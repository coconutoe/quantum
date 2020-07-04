from projectq import MainEngine
from projectq.ops import H, X, Y, Z, CNOT, Measure, All
import datetime

# create a main compiler engine
qbits = 18
eng = MainEngine()
qs = eng.allocate_qureg(qbits)

start = datetime.datetime.now()

H | qs[0]
for i in range(1, qbits):
    CNOT | (qs[0], qs[i])

# 1, @18H :

for i in range(0, qbits):
    H | qs[i]


# 2, @18X :
'''
for i in range(0, qbits):
    X | qs[i]
'''

# 3, @18Y :
'''
for i in range(0, qbits):
    Y | qs[i]
'''

# 4, @18Z :
'''
for i in range(0, qbits):
    Z | qs[i]
'''

# 5, @9H_9X :
'''
for i in range(0, int(qbits / 2)):
    H | qs[i]
for i in range(int(qbits / 2), qbits):
    X | qs[i]
'''

# 6, @9H_9Y :
'''
for i in range(0, int(qbits / 2)):
    H | qs[i]
for i in range(int(qbits / 2), qbits):
    Y | qs[i]
'''

# 7, @9H_9Z :
'''
for i in range(0, int(qbits / 2)):
    H | qs[i]
for i in range(int(qbits / 2), qbits):
    Z | qs[i]
'''

# 8, @6X_6Y_6Z :
'''
for i in range(0, int(qbits / 3)):
    X | qs[i]
for i in range(int(qbits / 3), 2 * int(qbits / 3)):
    Y | qs[i]
for i in range(2 * int(qbits / 3), qbits):
    Z | qs[i]
'''

All(Measure) | qs
eng.flush()

end = datetime.datetime.now()
print(end - start)

print("Measured: {}".format(int(qs[0])))
print("Measured: {}".format(int(qs[1])))
