#!/bin/bash

#!/bin/bash

if [ -e "venv" ]; then
    ./venv/bin/python ./python_code/web_server/zmq_server.py
else
    echo "No virtual environment configured"
    echo "Please run ./set_up.sh first"
fi
