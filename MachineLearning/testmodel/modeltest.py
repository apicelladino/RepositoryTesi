from tensorflow.python.lib.io import file_io
import argparse
from pandas import read_csv
from sklearn.preprocessing import LabelEncoder, MinMaxScaler
from keras.utils import np_utils
from keras.models import Sequential
from keras.layers import Dense, Activation, Dropout, LSTM, Conv1D
from keras.callbacks import TensorBoard, ModelCheckpoint
from numpy import array, split

import keras.backend as k
import tensorflow as tf
from keras.models import load_model
from tensorflow.python.saved_model import builder as saved_model_builder
from tensorflow.python.saved_model import tag_constants, signature_constants
from tensorflow.python.saved_model.signature_def_utils_impl import predict_signature_def
# training parameters
epochs = 1
batch_size = 100
validation_split = 0.2

# model parameters
dropout = 0.2
timesteps = 40
timesteps_in_future = 20
nodes_per_layer = 32
filter_length = 3

def train_model(train_file='output1.csv', job_dir='', **args):
    parameter_string = '' + '_dropout_' + str(dropout) + '_timesteps_' + str(timesteps) + '_timesteps_in_future_' + str(timesteps_in_future) + '_nodes_per_layer_' + str(
                                                                                                                                                                                                nodes_per_layer) + '_filter_length_' + str(filter_length)
                                                                                                                                                                                                
#load data
#file_stream = file.io.FileIO(train_file, mode='r')
    file_stream = open(train_file,'r')
    dataframe = read_csv(file_stream, header=0)
    dataframe.fillna(0, inplace=True)
    dataset= dataframe.values
    X= dataset[:, [
                   0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11   #valori degli accelerometri
                   ]].astype(float)
    y = dataset[:, 12]   #ExerciseType è 12, ExerciseSubType è 13
#data parameters
    data_dim = X.shape[1]
    num_classes = len(set(y))
#scale x
    scaler = MinMaxScaler(feature_range=(0,1))
    X = scaler.fit_transform(X)
#encode Y
    encoder = LabelEncoder()
    encoder.fit(y)
    encoded_y = encoder.transform(y)
    hot_encoded_y = np_utils.to_categorical(encoded_y)
#prepare data for lstm
    def create_LSTM_dataset(x, y, timesteps):
        dataX, dataY = [],[]
        for i in range(len(x) - timesteps + 1):
                 dataX.append(x[i:i + timesteps, :])
                 dataY.append(y[i + timesteps - timesteps_in_future - 1, :])
        return array(dataX), array(dataY)
    X, hot_encoded_y = create_LSTM_dataset(X, hot_encoded_y, timesteps)
#define model
    model = Sequential([
                        Conv1D(nodes_per_layer, filter_length, strides=2, activation='relu', input_shape=(timesteps, data_dim), name='accelerations'),
                         Conv1D(nodes_per_layer, filter_length, strides=1, activation='relu'),
                         LSTM(nodes_per_layer, return_sequences=True),
                         LSTM(nodes_per_layer, return_sequences=False),
                         Dropout(dropout),
                         Dense(num_classes),
                         Activation('softmax', name='scores'),
     ])
    model.summary()
#compile model
    model.compile(optimizer='rmsprop',
                    loss='categorical_crossentropy',
                    metrics=['accuracy'])
#define callbacks
    callbacks = []
    tensor_board = TensorBoard(log_dir='./test_logs', histogram_freq=1, write_graph=False, write_images=False)
    callbacks.append(tensor_board)
    checkpoint_path = 'best_weights.h5'
    checkpoint = ModelCheckpoint(checkpoint_path, monitor='val_acc', verbose=1, save_best_only=True, mode='max')
    callbacks.append(checkpoint)
#train model
    model.fit(X, hot_encoded_y,
                batch_size=batch_size,
                epochs=epochs,
                verbose=1,
                validation_split=validation_split,
                callbacks=callbacks
                )
    print(model.output.op.name)
    saver = tf.train.Saver()
    saver.save(k.get_session(), '$(pwd)/keras_model.ckpt')
#load best checkpoint
    model.load_weights('best_weights.h5')
#evaluate best model
    def non_shuffling_train_test_split(X, y, test_size=validation_split):
            i = int((1 - test_size) * X.shape[0]) + 1
            X_train, X_test = split(X, [i])
            y_train, y_test = split(y, [i])
            return X_train, X_test, y_train, y_test
    _, X_test, _, y_test = non_shuffling_train_test_split(X, hot_encoded_y, test_size=validation_split)
    scores = model.evaluate(X_test, y_test, verbose=0)
    acc = scores[1]
#save model
    model_h5_name = 'model_acc_' + str(acc) + '.h5'
    model.save(model_h5_name)
if __name__ == '__main__':
           parser = argparse.ArgumentParser()
           parser.add_argument(
                 '--train-file',
                 help='GCS or local paths to training data',
                 required=True
            )
           parser.add_argument(
                 '--job-dir',
                 help='GCS location to write checkpoints and export models',
                 required=True
            )
           args = parser.parse_args()
           arguments = args.__dict__
           train_model(**arguments)






















