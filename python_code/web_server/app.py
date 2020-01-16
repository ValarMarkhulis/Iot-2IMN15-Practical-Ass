#!/usr/bin/env python


from flask import Flask, render_template
from flask_socketio import SocketIO, send

from sqlite_client import get_db, query_db


app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app)


@app.route('/')
def home():
    return render_template("index.html")


@socketio.on('get_cars')
def handle_message(message):
    r = query_db("SELECT COUNT(*) FROM PARKING_SPOTS WHERE spot_state=1")[0][0]
    send(r)


if __name__ == '__main__':
    get_db().execute("CREATE TABLE IF NOT EXISTS PARKING_SPOTS(id int PRIMARY KEY, spot_state int, lot_name varchar(128))")
    socketio.run(app)
