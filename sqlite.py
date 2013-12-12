import sqlite3
import csv
conn = sqlite3.connect("development.db")
conn.text_factory = str
c = conn.cursor()
id = 0
level = 2
with open("level2.csv",'rU') as csvfile:
  reader = csv.reader(csvfile)
  for row in reader:
    if len(row[4])==4:
      row[4]='6499 '+row[4]
    newRow = [row[3],level,id]
    insert = tuple(newRow)
    c.execute("""insert into locations(rname,level,_id)
              values (?,?,?)""",insert)
    id+=1;
#c.execute('''create table offices
#(room_number text, level text, name text,
# number text, email text, x integer, y integer)''')
conn.commit()
c.close()
