import tensorflow as tf
import numpy as np
from numpy import array
import os
import csv


frozen_graph="./model_frozen.pb"
#Load frozen graph
with tf.gfile.GFile(frozen_graph, "rb") as f:
    restored_graph_def = tf.GraphDef()
    restored_graph_def.ParseFromString(f.read())
with tf.Graph().as_default() as graph:
    tf.import_graph_def(
        restored_graph_def,
        input_map=None,
        return_elements=None,
        name=""
        )
        #x, y= tf.import_graph_def(restored_graph_def, return_elements=['accelerations_input:0',
#  'scores/Softmax:0'],
#name='')
y_pred = graph.get_tensor_by_name("scores/Softmax:0")
x = graph.get_tensor_by_name("accelerations_input:0")
print(x)

          

#y_test = np.random.rand(1,40,12)
sess = tf.Session(graph=graph)

with tf.Session(graph=graph) as sess:
    
        with open('input_data.csv') as f:
            reader = csv.reader(f)
            data_read = [row for idx, row in enumerate(reader) if idx in (0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,177,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39)]
        print("Output probability")
        data_read = array(data_read).reshape(1,40,12)
        y_out = sess.run(y_pred, feed_dict={
                            x: data_read
                            })


print(y_out)
print(y_pred)
best_prediction = y_out.argmax()
print(best_prediction)
