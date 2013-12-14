import sqlite3
import csv
conn = sqlite3.connect("development.db")
conn.text_factory = str
c = conn.cursor()
c.execute("select MAX(_id) as _id from people")
#id = c.fetchone()[0]+1
c = conn.cursor()
# _id,name,position,email,number,location_id

def insert_room(c,row,id):
  newRow = [row[4],row[5],id]
  insert = tuple(newRow)
  c.execute("""INSERT INTO locations(rname,level,_id) 
               VALUES (?,?,?)""",insert)

def populate():
  id = 0
  with open("full.csv",'rU') as csvfile:
    reader = csv.reader(csvfile)
    for row in reader:
      if row[0]=="":
        continue
      if len(row[3])==4:
        row[3]='6499'+row[4]
      location_id=get_location_id(row[4],row[5])
      insert_people(c,row,id,location_id)

      id+=1;
  conn.commit()
  c.close()


def link_room_to_person(roomid, personid):
  c = conn.cursor().execute("""UPDATE locations SET user_id='%s' WHERE _id='%s'"""%(personid,roomid))

def get_location_id(rname,level):
  if rname == "" or not rname:
    return ""
  print rname,level
  return conn.cursor().execute("""select _id 
                           from locations 
                           where rname='%s' and 
                           level='%s' """%(rname,level)).fetchone()[0]    

def insert_people(c,row,id,location_id):
  newRow = [row[0],row[1],row[2],row[3],location_id,id]
  insert = tuple(newRow)
  c.execute("""insert into people(name,position,email,number,location_id,_id) 
               values (?,?,?,?,?,?)""",insert)


populate()

"""SELECT * 
   FROM people 
   WHERE location_id 
   IN (
     SELECT _id 
     FROM locations 
     WHERE rname="R3");"""

