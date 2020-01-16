import sqlite3


DATABASE = "cars.db"


def get_db():
    db = sqlite3.connect(DATABASE)
    return db


def exec_q(query):
    db = get_db()
    c = db.cursor()
    c.execute(query)
    db.commit()
    db.close()


def query_db(query, args=(), one=False):
    cur = get_db().execute(query, args)
    rv = cur.fetchall()
    cur.close()
    return (rv[0] if rv else None) if one else rv
