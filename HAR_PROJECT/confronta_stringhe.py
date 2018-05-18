import os
import csv
from itertools import islice
i=0
f = open("Previsions.csv","w+")
breakstr = "Break"
burpeestr = "Burpee"
situpstr = "Situp"
squatstr = "Squat"

with open("prevision_test.csv") as file:
        breakcount = 0
        burpeecount = 0
        situpcount = 0
        squatcount = 0
        linecount = 0
        for line in file:
          
            if  (breakstr in line and linecount<60):
                    breakcount = breakcount+1
                    linecount = linecount+1
            if (burpeestr in line and linecount<60):
                    burpeecount = burpeecount+1
                    linecount = linecount+1
            if (situpstr in line and linecount<60):
                    situpcount = situpcount+1
                    linecount = linecount+1
            if (squatstr in line and linecount<60):
                    squatcount = squatcount+1
                    linecount = linecount+1
            if (linecount==60):
                linecount=0
                highest = max(breakcount,burpeecount,situpcount,squatcount)
                print(highest)
                i=i+1
                if highest == breakcount:
                    f.write("Break è la numero : "+str(i)+"\n")
                if highest == burpeecount:
                    f.write("Burpee è la numero : "+str(i)+"\n")
                if highest == situpcount:
                    f.write("Situp è la numero : "+str(i)+"\n")
                if highest == squatcount:
                    f.write("Squat è la numero : "+str(i)+"\n")
                breakcount=0
                squatcount=0
                burpeecount=0
                situpcount=0
f.close()
