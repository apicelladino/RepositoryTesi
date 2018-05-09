import os
import csv
#filehandle = open(filename,"r")
#while True:
# line = filehandle.readline()
# if not line:
#       break
#  print(line)
#filehandle.close()
#reader = csv.reader(open(filename), delimiter=',', quoting=csv.QUOTE_NONE)
    #for row in reader:
#print(', '.join(row))
#while True:
    #row1 = next(reader)
    # for row in row1:
    #    vals = row.split(',')
#     print(vals[0])
    #rssi = vals[2]
#print("right hand: rssi:",rssi)
f2=open("input_data.csv","w+")
f = open("datanormalized.csv","r")
myreader = csv.reader(f)
for row in myreader:
    #print(row[54]+","+row[55]+","+row[56]+","+row[57]+","+row[58]+","+row[59]+","+row[60]+","+row[61]+","+row[62]+","+row[63]+","+row[64]+","+row[65]+","+row[66]+","+row[67]+","+row[68]+","+row[69])
               f2.write(row[2]+","+row[3]+","+row[4]+","+row[5]+","+row[6]+","+row[7]+","+row[8]+","+row[9]+","+row[10]+","+row[11]+","+row[12]+","+row[13]+"\n")
f2.close()
