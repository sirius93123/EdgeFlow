# EdgeFlow

Demo for EdgeFlow framework.

## Usage

First, load a `config.txt` file in the project of the main function. 
Each line represents a configuration to run against,
which consists of the following information: 
the layer number,
computing capacity (for simulate the difference of different layer),
data generation rate, 
the IP and Port of upper layer node,
the IP and Port of the CC layer node,
the task-offloading strategy parameter.

Third, follow the bash commands below and initialize the system from Cloud layer to Edge Device layer in order.

### Cloud

```
python tunnel.py
java -jar CC.jar
```

### Access Point

```
python tunnel.py
java -jar AP.jar
```

### Edge Device

```
python tunnel.py
java -jar ED.jar
```
