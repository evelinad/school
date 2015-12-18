#! /usr/bin/python

import matplotlib.pyplot as plt

loudness = [line.split() for line in open("task1.in", "r").readlines()]
print loudness
plt.plot([int(elem[0]) for elem in loudness[1:]], [float(elem[1]) for elem in loudness[1:]], label="Loudness")
plt.grid(True)
plt.legend()
plt.title("Loudness evolution over years")
plt.show()


