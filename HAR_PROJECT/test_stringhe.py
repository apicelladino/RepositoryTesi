import os
import csv
from itertools import islice
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
f2=open("prevision_test.csv","w+")
k=0
with open("datanormalized.csv") as f:

    for row in islice(csv.reader(f), 0 , None):
        f2.write(row[0]+"\n")
    #print(row[54]+","+row[55]+","+row[56]+","+row[57]+","+row[58]+","+row[59]+","+row[60]+","+row[61]+","+row[62]+","+row[63]+","+row[64]+","+row[65]+","+row[66]+","+row[67]+","+row[68]+","+row[69])
#  f2.write(row[2]+"\n"+row[3]+"\n"+row[4]+"\n"+row[5]+"\n"+row[6]+"\n"+row[7]+"\n"+row[8]+"\n"+row[9]+"\n"+row[10]+"\n"+row[11]+"\n"+row[12]+"\n"+row[13]+"\n")
        #  k=k+1
        #if k is 50 :
#    break
f2.close()
