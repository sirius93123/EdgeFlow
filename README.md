# EdgeFlow

Demo for EdgeFlow framework.

## Usage

First, create a `config.txt` file in Edge Device. Each line represents a configuration to run against, which consists of the following information: ED computing capacity, AP computing capacity, CC computing capacity, data generation rate, data compression rate and package size. To run a burst test, set the burst flag in `ED.java` as true. It will generate two data bursts automatically during the emulation. Otherwise, the burst scenario is disabled.

Second, allocate static IP addresses for each device, and edit the python files accordingly if necessary.

Third, follow the bash commands below and initialize the system from Cloud layer to Edge Device layer in order.

### Cloud

```
javac CC.java
python usrp_base.py
java CC
```

### Access Point

```
javac AP.java
python usrp_base.py
python usrp_client.py
java AP
```

### Edge Device

```
javac ED.java
python usrp_client.py
java ED
```
