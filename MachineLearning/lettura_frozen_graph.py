import tensorflow as tf
from tensorflow.python.platform import gfile
with tf.Session() as sess:
    model_filename ="frozen_graph.pb"
    with gfile.FastGFile(model_filename, 'rb') as f:
        graph_def = tf.GraphDef()
        graph_def.ParseFromString(f.read())
        g_in = tf.import_graph_def(graph_def)
writer=tf.summary.FileWriter("./frozen_logs",sess.graph)
merged = tf.summary.merge_all()
print("done")
