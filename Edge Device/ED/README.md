# EdgeFlow

Demo for EdgeFlow framework.

## Usage

First, create a `config.txt` file in Edge Device. Each line represents a configuration to run against, which consists of the following information: computing capacity, data generation rate, communication speed, data compression rate and package size, the IP of upper layer node. 

Third, follow the bash commands below and initialize the system from Cloud layer to Edge Device layer in order.

### Cloud

```
python virtual_link.py
java -jar CC.jar CC.config
```

### Access Point

```
python usrp_base.py
java -jar AP.jar AP.config
```

### Edge Device

```
python usrp_client.py
java -jar ED.jar ED.config
```
