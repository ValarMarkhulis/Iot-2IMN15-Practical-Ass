#!/usr/bin/env python

from sense_hat import SenseHat

from time import sleep

sense = SenseHat()
red = (255, 0, 0)
green = (0, 255, 0)

sense.clear()
sense.show_letter("F", green)            # The spot is free

while True:
    for event in sense.stick.get_events():
        # Check if the joystick was pressed
        if (event.action == "pressed" and event.direction == "middle"):
            sense.show_letter("O", red)     # The spot is occuiped
            print("Occuiped")
        elif event.action == "released":
            sense.show_letter("F", green)   # Enter key
            print("Free")
    sleep(0.1)
