#!/usr/bin/env python

from sense_hat import SenseHat

from led_matrix_base import G as GREEN
from led_matrix_base import O as ORANGE
from led_matrix_base import R as RED
from led_matrix_base import (
    flush_color,
    get_direction
)


color_param, direction = get_direction()

if color_param == "G":
    color = GREEN
elif color_param == "O":
    color = ORANGE
elif color_param == "R":
    color = RED


s = SenseHat()
s.set_pixels(flush_color(color, direction))
