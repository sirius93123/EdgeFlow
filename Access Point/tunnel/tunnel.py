#!/usr/bin/env python
## need update
import os, sys
import hashlib
import getopt
import fcntl
import icmp
import time
import struct
import socket, select
import random
import time

import ofdm.TDMATunnel

SHARED_PASSWORD = hashlib.md5("xiaoxia").digest()
TUNSETIFF = 0x400454ca
IFF_TUN   = 0x0001

MODE = 0
DEBUG = 0
PORT = 0
IP="192.168.10.4"
IFACE_IP = "10.0.0.1"
MTU = 1500
CODE = 86
TIMEOUT = 60*10 # seconds

class Tunnel():
    def create(self):
        self.tfd = os.open("/dev/net/tun", os.O_RDWR)
        ifs = fcntl.ioctl(self.tfd, TUNSETIFF, struct.pack("16sH", "t%d", IFF_TUN))
        self.tname = ifs[:16].strip("\x00")
        self.clients = []
        self.usrpFlag = True
        self.percentage = 0.5
        self.time1 = 0
        self.flag = 1
        self.TrueFlag = 1
        self.mac = 0
        print(self.clients)
    def macIni(self, mac):
        self.mac = mac
    def macRx(self,ok, payload):
        buf = self.icmpfd.recv(icmp.BUFFER_SIZE)
        data = packet.parse(buf, DEBUG)
        ip = socket.inet_ntoa(packet.src)
        os.write(self.tfd, data)

    def close(self):
        os.close(self.tfd)

    def config(self, ip):
        os.system("ip link set %s up" % (self.tname))
        os.system("ip link set %s mtu 1000" % (self.tname))
        os.system("ip addr add %s dev %s" % (ip, self.tname))

    def serverRun(self):
        self.time1 = time.time()
        self.icmpfd = socket.socket(socket.AF_INET, socket.SOCK_RAW, socket.getprotobyname("icmp"))
        packet = icmp.ICMPPacket()
        self.client_seqno = 1

        while True:
            rset = select.select([self.icmpfd, self.tfd], [], [])[0]
            if time.time() - time1 > 0.05:
                time1 = time.time()
                if random.random() > self.percentage:
                    self.flag = 1
                else:
                    self.flag = 0
                data = self.flag
                buf = packet.create(8, 90, 8000, 1, data)
                for ip in self.clients:
                        #    print(ip)
                    self.icmpfd.sendto(buf, (ip, 22))

            for r in rset:
                if r == self.tfd:
                    #print("tun")
                    if DEBUG: os.write(1, ">")
                    data = os.read(self.tfd, MTU)
                    if MODE == 1: # Server
                        buf = packet.create(8, 87, 8000, 1, data)
                        for ip in self.clients:
                            self.icmpfd.sendto(buf, (ip, 22))
                elif r == self.icmpfd:
                    if DEBUG: os.write(1, "<")
                    buf = self.icmpfd.recv(icmp.BUFFER_SIZE)
                    data = packet.parse(buf, DEBUG)
                    ip = socket.inet_ntoa(packet.src)
                    #print("WWWWWWWWWWWWWWWWWWWW")
                    if packet.code in (CODE, CODE+1):
                         # Simply write the packet to local or forward them to other clients ???
                         os.write(self.tfd, data)
                         #self.clients[key]["aliveTime"] = time.time()



    def run(self):
        if MODE == 1:
            self.serverRUN()

        self.icmpfd = socket.socket(socket.AF_INET, socket.SOCK_RAW, socket.getprotobyname("icmp"))
        packet = icmp.ICMPPacket()
        self.client_seqno = 1

        dataFlag = False
        datatemp = 0
        buf = 0
        while True:
            rset = select.select([self.icmpfd, self.tfd], [], [])[0]
            for r in rset:
                if r == self.tfd:
                    #print("tun")
                    if DEBUG: os.write(1, ">")
                    if dataFlag == False:
                        datatemp = os.read(self.tfd, MTU)
                        buf = packet.create(8, 86, 8000, 1, datatemp)
                        dataFlag = True
                    if self.usrpFlag == True:
                        if len(datatemp) > 500:
                            self.mac.sendto(buf)
                        else:
                            self.icmpfd.sendto(buf, (IP, 22))
                        dataFlag = False
                elif r == self.icmpfd:
                    if DEBUG: os.write(1, "<")
                    buf = self.icmpfd.recv(icmp.BUFFER_SIZE)
                    data = packet.parse(buf, DEBUG)
                    ip = socket.inet_ntoa(packet.src)
                    #print("WWWWWWWWWWWWWWWWWWWW")
                    if packet.code in (CODE, CODE+1):
                         # Simply write the packet to local or forward them to other clients ???
                         os.write(self.tfd, data)

                    if packet.code in (90, 91):
                          if data[0] == self.TrueFlag:
                             self.usrpFlag = True
                           else:
                              self.usrpFlag = False
                         #self.clients[key]["aliveTime"] = time.time()

def usage(status = 0):
    print "Usage: icmptun [-s code|-c serverip,code,id] [-hd] [-l localip]"
    sys.exit(status)

if __name__=="__main__":
    opts = getopt.getopt(sys.argv[1:],"s:c:l:hd")
    for opt,optarg in opts[0]:
        if opt == "-h":
            usage()
        elif opt == "-d":
            DEBUG += 1
        elif opt == "-s":
            MODE = 1
            CODE = int(optarg)
        elif opt == "-c":
            MODE = 2
            IP,CODE,PORT = optarg.split(",")
            CODE = int(CODE)
            PORT = int(PORT)
        elif opt == "-l":
            IFACE_IP = optarg

    if MODE == 0 or CODE == 0:
        usage(1)

    tun = Tunnel()
    tun.create()
    print "Allocated interface %s" % (tun.tname)
    tun.config(IFACE_IP)
    mac = MacInit(tun.macRx)
    tun.macIni(mac)
    try:
        tun.run()
    except KeyboardInterrupt:
        tun.close()
        sys.exit(0)

