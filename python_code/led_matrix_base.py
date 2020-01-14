#!/usr/bin/env python

import sys

G = green = (0, 255, 0)
R = red = (255, 0, 0)
B = blue = (0, 0, 255)
O = orange = (255, 165, 0) # noqa
N = nothing = (0, 0, 0)


def get_direction():
    if len(sys.argv) == 1:
        print("Parameters: \n\tcolor(mandatory)=[G,O,R], \n\tdirection=[1,0]")
        sys.exit()

    color = sys.argv[1]

    if len(sys.argv) > 2:
        arr_dir = sys.argv[2]
    else:
        arr_dir = None

    direction = ""
    if arr_dir == "0":
        direction = "right"
    elif arr_dir == "1":
        direction = "left"

    return color, direction


def flush_color(color, direction=""):
    logo = [color] * 48 + arrow(direction)
    return logo


def arrow(direction=""):
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
