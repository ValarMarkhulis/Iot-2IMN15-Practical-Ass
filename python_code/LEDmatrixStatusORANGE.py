from sense_hat import SenseHat
import time
import sys

s = SenseHat()
direction = ""
if len(sys.argv) < 2:
    input = -1
else:
    input = sys.argv[1]

green = (0, 255, 0)
red = (255, 0, 0)
orange = (255,165,0)
blue = (0, 0, 255)
nothing = (0,0,0)

G = green
R = red
B = blue
O = orange
N = nothing

if input == "0":
    direction = "right"
elif input == "1":
    direction = "left"

def reserved_space():

    logo = [
               O, O, O, O, O, O, O, O,
               O, O, O, O, O, O, O, O,
               O, O, O, O, O, O, O, O,
               O, O, O, O, O, O, O, O,
               O, O, O, O, O, O, O, O,
               O, O, O, O, O, O, O, O,
           ]+arrow()
    return logo

def arrow():
    if direction == "left":
        arrow = [
            B, B, B, B, B, B, B, B,
            N, B, N, N, N, N, N, N,
        ]
    elif direction == "right":
        arrow = [
            B, B, B, B, B, B, B, B,
            N, N, N, N, N, N, B, N,
        ]
    else:
        arrow = [
            N, N, N, N, N, N, N, N,
            N, N, N, N, N, N, N, N,
        ]


    return arrow

s.set_pixels(reserved_space())

