#!/usr/bin/env python

import os, sys
import hashlib
import getopt
import fcntl
import icmp
import time
import struct
import socket, select

SHARED_PASSWORD = hashlib.md5("xiaoxia").digest()
TUNSETIFF = 0x400454ca
IFF_TUN   = 0x0001

MODE = 0
DEBUG = 0
PORT = 0
IFACE_IP = "10.0.0.1"
MTU = 1500
CODE = 86
TIMEOUT = 60*10 # seconds

class Tunnel():
    def create(self):
        self.tfd = os.open("/dev/net/tun", os.O_RDWR)
        ifs = fcntl.ioctl(self.tfd, TUNSETIFF, struct.pack("16sH", "t%d", IFF_TUN))
        self.tname = ifs[:16].strip("\x00")

    def close(self):
        os.close(self.tfd)

    def config(self, ip):
        os.system("ip link set %s up" % (self.tname))
        os.system("ip link set %s mtu 1000" % (self.tname))
        os.system("ip addr add %s dev %s" % (ip, self.tname))

    def run(self):
        self.icmpfd = socket.socket(socket.AF_INET, socket.SOCK_RAW, socket.getprotobyname("icmp"))

        self.clients = {}
        packet = icmp.ICMPPacket()
        self.client_seqno = 1

        while True:
            rset = select.select([self.icmpfd, self.tfd], [], [])[0]
            for r in rset:
                if r == self.tfd:
                    print("Test")
                elif r == self.icmpfd:
                    #if DEBUG: os.write(1, "<")
                    buf = self.icmpfd.recv(icmp.BUFFER_SIZE)
                    print("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
                    print(socket.inet_ntoa(buf[12:16]))
                    print(socket.inet_ntoa(buf[16:20]))
                    data = packet.parse(buf, DEBUG)
                    ip = socket.inet_ntoa(packet.src)
                    print(data)
                    #if packet.code in (CODE, CODE+1):
                         # Simply write the packet to local or forward them to other clients ???
                    #     os.write(self.tfd, data)
                    #     self.clients[key]["aliveTime"] = time.time()

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
    try:
        tun.run()
    except KeyboardInterrupt:
        tun.close()
        sys.exit(0)

