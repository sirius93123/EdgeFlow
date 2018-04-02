import socket
import os, sys
import random, time, struct
import Queue
import socket
from datetime import datetime
import time
import io
import threading

class cs_mac(object):
    """
    Prototype carrier sense MAC

    Reads packets from the TUN/TAP interface, and sends them to the PHY.
    Receives packets from the PHY via phy_rx_callback, and sends them
    into the TUN/TAP interface.

    Of course, we're not restricted to getting packets via TUN/TAP, this
    is just an example.
    """
    def __init__(self):
        #self.tun_fd = tun_fd       # file descriptor for TUN/TAP interface
        #self.verbose = verbose
        #self.cond = cond      # top block (access to PHY) 
        self.flag = 0
        self.receive = 0
        self.tb = None       
        self.path_file = ""
        self.path_stream = 0
        self.send_no = 0
        self.strategy_slot = 1
        self.send_flag = 0
        self.queue = Queue.Queue(maxsize = 100)
        self.packet_no = 0
        self.address1 = "127.0.0.1"
        self.port1 = 9500
        self.address2 = "127.0.0.1"
        self.port2 = 9501
        self.servSock = 0
        self.socket2 = socket.socket(socket.AF_INET,socket.SOCK_STREAM)

    def __del__(self):
        self.socket2.close()
    def clientServer(self, client):
        while True:
              data_read  = client.recv(1400)
              if packetcase == 0:
                 usrp_send_packet()

    def socketServer(self):
        sock1 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock1.setsockopt(socket.IPPROTO_TCP,socket.TCP_NODELAY,1)
        sock1.bind((self.address2, self.port2))
        sock1.listen(1)
        client, address = sock1.accept()
        print("get the client")
        self.socket2 = client
        while True:
              data  = client.recv(1400)
              print(data)
              self.queue.put(data)

    def decodePacket(self, packet):
        return packetData

    def checkCommand(self, packet, command):
        (pktno,) = struct.unpack('!H', packet[0:2])
        print(int(packet[2]))
        if pktno == 0 and int(packet[2]) == (command):
            print("return true")
            return True 
        return False

    def listenTest(self):
        self.socketServer()
        #threading.Thread(target=self.socketServer).start()
        #while True:
        #      time.sleep(0.01)

    ## listen the command from the server 
    def listen(self, serverAddress, port):
        address = (serverAddress,port)
        max_size =1000
        print(address)
        print("Start the client at {}".format(datetime.now()))
        server = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        server.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY, 1) 
        try:
            server.connect(address)
        except:
            print("error")
            self.__del__()
        self.servSock = server
        threading.Thread(target=self.socketServer).start()
        while True:
            data = server.recv(max_size)
            #print("start send packet")
            self.send_packet()

    ## 10ms send guding de shujubao  ceshi yixai  
    ## 10ms 2 * 1400 is ok???
    def send_packet(self):
       # print("send queue")
        number = 2
        while number > 0:
              if not self.queue.empty():
                  payload2 = self.queue.get()
                  self.servSock.sendall(payload2)
                  #self.tb.txpath.send_pkt(payload2)
              number = number - 1
        #payload = struct.pack('!H', self.packet_no & 0xffff) + data
        #number = 2
        #while number > 0:
        #      self.tb.txpath.send_pkt(payload2)
        #      number = number - 1

mac = 0
def main():

    mac = cs_mac()
    mac.listen("192.168.0.5",9006)    # don't expect this to return...

if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print("qqq")
        del mac
        pass

