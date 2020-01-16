* To set up the virtualenvironment, use the virtualenv command. If it's not
    available, it may need to be installed too.
* Run `virtualenv -p /usr/bin/python3 venv` to create a folder named *venv*.
* Source the environment by running `source venv/bin/activate` or `.
    venv/bin/activate` ("." is equivalent to "source")
* You should see a "(venv)" at the left of your shell to make sure the
    environment is sourced.
* Run `pip install -r requirements.txt` to install dependencies.
* Run `./app.py` to start the *web server* and `./zm_test.py` to run the *zmq
    server* to listen for requests from *parking server*

