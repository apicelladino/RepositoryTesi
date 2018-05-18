import os
import csv
from itertools import islice

f = open("risultat_test_android.csv","w+")
with open("prove.csv") as file:
    for line in file:
        cleanedLine = line.strip()
        if cleanedLine:
            f.write(cleanedLine+"\n")
f.close
