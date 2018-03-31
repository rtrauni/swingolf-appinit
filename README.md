# swingolf-appinit

# goal 
- read players
- read tournaments
- read scores

# neo4j upload

# program arguments
-Dplayer.directory=C:\Users\rudolftraunmueller\git\swingolf-appinit\src\main\resources\players\ -Dresult.directory=C:\Users\rudolftraunmueller\git\swingolf-appinit\src\main\resources\results\2017

-Xmx4000m
-Dplayer.directory="/home/rudolftraunmueller/git/swingolf-appinit/src/test/resources/players/"
-Dresult.directory="/home/rudolftraunmueller/git/swingolf-appinit/src/test/resources/results/"

# excel export
openoffice
save Handicap_Lizenzspieler as csv
field delimiter ,
text delimiter empty
rename to player 2018.csv