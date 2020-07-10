from qiskit import QuantumCircuit, ClassicalRegister, QuantumRegister
from qiskit import execute, Aer
from qiskit.tools.visualization import circuit_drawer
import datetime

qbits = 18
start = datetime.datetime.now()

circ = QuantumCircuit(qbits)
backend = Aer.get_backend('statevector_simulator')

circ.h(0)
for i in range(1, qbits):
    circ.cx(0, i)

# 1, @18H :
'''
for i in range(0, qbits):
    circ.h(i)
'''

# 2, @18X :

for i in range(0, qbits):
    circ.x(i)

'''
# 3, @18Y :
for i in range(0, qbits):
    circ.y(i)
'''

# 4, @18Z :
'''
for i in range(0, qbits):
    circ.z(i)
'''

# 5, @9H_9X :
'''
for i in range(0, int(qbits / 2)):
    circ.h(i)
for i in range(int(qbits / 2), qbits):
    circ.x(i)
'''

# 6, @9H_9Y :
'''
for i in range(0, int(qbits / 2)):
    circ.h(i)
for i in range(int(qbits / 2), qbits):
    circ.y(i)
'''

# 7, @9H_9Z :
'''
for i in range(0, int(qbits / 2)):
    circ.h(i)
for i in range(int(qbits / 2), qbits):
    circ.z(i)
'''

# 8, @6X_6Y_6Z :
'''
for i in range(0, int(qbits / 3)):
    circ.x(i)
for i in range(int(qbits / 3), 2 * int(qbits / 3)):
    circ.y(i)
for i in range(2 * int(qbits / 3), qbits):
    circ.z(i)
'''

job = execute(circ, backend)
result = job.result()
outputstate = result.get_statevector(circ, decimals=3)

end = datetime.datetime.now()
print(end - start)

