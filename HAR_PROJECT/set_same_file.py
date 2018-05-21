import os
import csv
file = open("android_results_with_set_break.txt","w+")
with open("risultat_test.csv") as f:
    for row in f:
        file.write(row[0]+row[1]+row[2]+row[3]+row[4]+row[5]+"\n")
file.close()
