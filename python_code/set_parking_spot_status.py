#!/usr/bin/env python

from sense_hat import SenseHat

from led_matrix_base import (
    get_pixel_params
)


x, y, color = get_pixel_params()

s = SenseHat()
s.set_pixel(x, y, color)
