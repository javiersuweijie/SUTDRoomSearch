import sqlite3
import csv
conn = sqlite3.connect("development.db")
conn.text_factory = str

def update():
  with open("level3.csv") as csvfile:
    reader = csv.reader(csvfile)
    for row in reader:
      c = conn.cursor().execute("""SELECT * FROM locations 
                   WHERE rname="%s" AND level="%s" """%(row[1],row[0]))
      if c.fetchone():
        update_coords(row)
      else:
        insert_location(row)
  check();
  conn.commit()
  conn.close()

def update_coords(row):
  conn.cursor().execute("""UPDATE locations 
                           SET xcoord="%s",ycoord="%s" 
                           WHERE rname="%s" AND level="%s";"""%(row[2],row[3],row[1],row[0]))


def insert_location(row):
    idc = conn.cursor().execute("""SELECT MAX(_id) as _id from locations""")
    id = idc.fetchone()[0]+1
    newRow = [row[0],row[1],row[2],row[3],id]
    insert = tuple(newRow)
    conn.cursor().execute("""INSERT INTO locations(level,rname,xcoord,ycoord,_id)
                             VALUES (?,?,?,?,?)""",insert)

def check():
  c=conn.cursor().execute("""SELECT * FROM locations WHERE rname="MR8" """)
  print c.fetchone()
  c=conn.cursor().execute("""SELECT * FROM locations WHERE level=3 """)
  print c.fetchone()
update()
