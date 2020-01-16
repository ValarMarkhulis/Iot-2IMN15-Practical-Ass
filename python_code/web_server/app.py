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
    occupied_count = query_db("SELECT COUNT(*) FROM PARKING_SPOTS WHERE spot_state='occupied'")[0][0]
    free_count = query_db("SELECT COUNT(*) FROM PARKING_SPOTS WHERE spot_state='free'")[0][0]
    reserved_count = query_db("SELECT COUNT(*) FROM PARKING_SPOTS WHERE spot_state='reserved'")[0][0]
    send({
        "occupied": occupied_count,
        "free": free_count,
        "reserved": reserved_count
    }, json=True)


if __name__ == '__main__':
    get_db().execute("CREATE TABLE IF NOT EXISTS PARKING_SPOTS(id varchar(128) PRIMARY KEY, spot_state varchar(8), lot_name varchar(128))")
    socketio.run(app, host="0.0.0.0")
