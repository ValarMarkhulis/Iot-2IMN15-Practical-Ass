#!/usr/bin/env python

import zmq

from sqlite_client import exec_q


if __name__ == "__main__":

    context = zmq.Context()

    #  Socket to talk to server
    socket = context.socket(zmq.REP)
    socket.bind("tcp://*:5555")

    #  Do 10 requests, waiting each time for a response
    try:
        while True:
            #  Get the reply.
            message = str(socket.recv())
            print("RECEIVED:", message)
            if "REG" in message:
                components = message.split()
                spot_id = str(components[1]).replace("'", "")
                state = str(components[2]).replace("'", "")
                name = str(components[3]).replace("'", "")
                query = """
                    INSERT INTO PARKING_SPOTS(id,spot_state,lot_name) VALUES('{0}', '{1}', '{2}')
                    ON CONFLICT(id) DO UPDATE SET
                    spot_state=excluded.spot_state,
                    lot_name=excluded.lot_name
                    WHERE excluded.id='{0}';
                """.format(spot_id, state, name)
                exec_q(query)
            socket.send(b"ACK")
    except KeyboardInterrupt:
        print("Stopping")
