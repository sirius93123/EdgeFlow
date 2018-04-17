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
max_size=1400

class Tunnel():
    def create(self, ofdm):
        self.tfd = os.open("/dev/net/tun", os.O_RDWR)
        ifs = fcntl.ioctl(self.tfd, TUNSETIFF, struct.pack("16sH", "t%d", IFF_TUN))
        self.tname = ifs[:16].strip("\x00")
        self.ofdm = ofdm
        self.TransmitKey = 0.5

    def close(self):
        os.close(self.tfd)

    def config(self, ip):
        os.system("ip link set %s up" % (self.tname))
        os.system("ip link set %s mtu 1500" % (self.tname))
        os.system("ip addr add %s dev %s" % (ip, self.tname))

    def parse(self, buf):
        dst=buf[20:24]
        return socket.inet_ntoa(dst)

    def run(self):
        self.udpfd=socket.socket(socket.AF_INET,socket.SOCK_DGRAM)
        self.udpfd.bind(("192.168.10.4",8000))
        self.udpfd.setblocking(False)
        epoll = select.epoll()
        epoll.register(self.udpfd.fileno(), select.EPOLLIN)
        epoll.register(self.tfd, select.EPOLLIN)
        tunfd=self.tfd
        no=1
        socketfd=self.udpfd.fileno()
        time0=0
        time1=0
        time2=9
        time3=0
        time4=90
        time5=0
        time6=0
        time7=9
        time8=0
        time9=0
        while True:
            if time1 - time9 >= 0.1:
                 if w = random.rand() > self.TransmitKey:
                    IP = "192.168.0.5"
                    self.udpfd.sendto("0000", (IP, 8000))
                    IP = "192.168.0.4"
                    self.udpfd.sendto("0001", (IP, 8000))
                 else:
                     IP = "192.168.0.4"
                     self.udpfd.sendto("0000", (IP, 8000))
                     IP = "192.168.0.5"
                     self.udpfd.sendto("0001", (IP, 8000))
            time1=time.time()
            epoll_list= epoll.poll(0.001)
            time2=time.time()
            print("EEEEEEEEEEEEEEEEEEEEEEEEEEEE")
            print(time2-time1)
            if not epoll_list:
                 print("no event")
                 continue
            for fd, event in epoll_list:
                if fd == tunfd:
                    print("Tun")
                if fd == socketfd:
                    print("socket")
            print("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT")
            for fd, event in epoll_list:
                if event & select.EPOLLIN:
                     if fd == tunfd and key == 1:
                        data=os.read(self.tfd, max_size)
                        time4=time.time()
                        if MODE == 1: # Server
                            dst=self.parse(data)
                            print(dst)
                            dst="192.168.10.3"
                            self.ofdm.usrpSend(data,(dst,8000))
                            print("AAAAAAAAAAAAA")
                            no=no+1
                            print(no)
                            #print("send success")
                        else: # Client
                            self.udpfd.sendto(data, (IP, 8000))
                        time5=time.time()
                     elif fd == socketfd:##
                        time6=time.time()
                        data,address= self.udpfd.recvfrom(max_size)
                        time7=time.time()
                        os.write(self.tfd, data)
                        time8=time.time()
                print("WWWWWWWWWWWWWWWWWWWWWWWWWw")
                print(time.time() - time9)
                if time.time()-time9 > 0.1:
                    print("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG")
                time9 = time.time()
                print("DDDD")
                print(time4 - time3)
                print("FFFFF")
                print(time5-time4)
                print("GGGGG")
                print(time7-time6)
                print("QQQQ")
                print(time8-time7)
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

    tun = Tunnel(mac, 0.7)
    tun.create()
    print "Allocated interface %s" % (tun.tname)
    tun.config(IFACE_IP)
    try:
        tun.run()
    except KeyboardInterrupt:
        tun.close()
        sys.exit(0)

