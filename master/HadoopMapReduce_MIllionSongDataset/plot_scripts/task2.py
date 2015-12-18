#! /usr/bin/python

import matplotlib.pyplot as plt

loudness = [line.split() for line in open("task2.in", "r").readlines()]
f, ax = plt.subplots(2, sharex=True)

ax[0].plot([int(elem[0]) for elem in loudness[1:]], [float(elem[1]) for elem in loudness[1:]], label="Major Loudness")
ax[0].plot([int(elem[0]) for elem in loudness[1:]], [float(elem[3]) for elem in loudness[1:]], label="Minor Loudness")
ax[1].plot([int(elem[0]) for elem in loudness[1:]], [float(elem[2]) for elem in loudness[1:]], label="Major Tempo")
ax[1].plot([int(elem[0]) for elem in loudness[1:]], [float(elem[4]) for elem in loudness[1:]], label="Minor Tempo")

ax[0].grid(True)
x1,x2,y1,y2 = ax[0].axis()
ax[0].axis((x1, x2, -35, 5))
x1,x2,y1,y2 = ax[1].axis()
ax[1].axis((x1, x2, -5, 190))
ax[1].grid(True)

ax[0].legend()
ax[0].set_title("Loudness evolution over years for major and minor keys")

ax[1].legend()
ax[1].set_title("Tempo evolution over years for major and minor keys")

plt.show()


