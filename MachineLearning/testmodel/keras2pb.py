python3 freeze_graph.py
--input_meta_graph=keras_model.ckpt.meta\
--input_checkpoint=keras_model.ckpt.index\
--output_graph=keras_frozen.pb\
--output_node_names="<output_node_name_printed_in_step_1>" \
--input_binary=true
