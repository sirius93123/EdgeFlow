#!/usr/bin/python2
#
# Copyright 2005,2006,2011 Free Software Foundation, Inc.
# 
# This file is part of GNU Radio
# 
# GNU Radio is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2, or (at your option)
# any later version.
# 
# GNU Radio is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with GNU Radio; see the file COPYING.  If not, write to
# the Free Software Foundation, Inc., 51 Franklin Street,
# Boston, MA 02110-1301, USA.
# 


# /////////////////////////////////////////////////////////////////////////////
#
#    This code sets up up a virtual ethernet interface (typically gr0),
#    and relays packets between the interface and the GNU Radio PHY+MAC
#
#    What this means in plain language, is that if you've got a couple
#    of USRPs on different machines, and if you run this code on those
#    machines, you can talk between them using normal TCP/IP networking.
#
# /////////////////////////////////////////////////////////////////////////////

import os, sys
import random, time, struct
import Queue
import socket
from datetime import datetime
import time
import threading

socket.setdefaulttimeout(None)

#print os.getpid()
#raw_input('Attach and press enter')


# /////////////////////////////////////////////////////////////////////////////
#
#   Use the Universal TUN/TAP device driver to move packets to/from kernel
#
#   See /usr/src/linux/Documentation/networking/tuntap.txt
#
# /////////////////////////////////////////////////////////////////////////////

# /////////////////////////////////////////////////////////////////////////////
#                           Carrier Sense MAC
# /////////////////////////////////////////////////////////////////////////////

class cs_mac(object):
    """
    Prototype carrier sense MAC

    Reads packets from the TUN/TAP interface, and sends them to the PHY.
    Receives packets from the PHY via phy_rx_callback, and sends them
    into the TUN/TAP interface.

    Of course, we're not restricted to getting packets via TUN/TAP, this
    is just an example.
    """
    def __init__(self, verbose=False):
        #self.tun_fd = tun_fd       # file descriptor for TUN/TAP interface
        self.verbose = verbose
        self.tb = None             # top block (access to PHY) 
        self.flag = 0
        self.receive = 0
        self.path_file = ""
        self.send_no = 0
        self.strategy_slot = 0.5
        self.strategy_judge = 0.5
        self.clients = []
        self.now_client = 0
        self.file_name = 0
        self.file_out_stream = 0
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY,1)
        self.sock.bind(('192.168.0.5', 9006))
        self.sock2 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.client2 = 0
        #self.socket3 = 
 
    def __del__(self):
        self.sock.close()

    def sendPacketToTop(self, socket, data):
        print("delete socekt")  

    def clientServer(self, client):
        while True:
              data_read  = client.recv(1400)
              print("get data")
              print(data_read)
              if self.client2 != 0:
                 self.client2.sendall(data_read)
              else: 
                 print("ale") 

              #if packetcase == 0:
              #   usrp_send_packet()  s                        

    def set_flow_graph(self, tb):
        self.tb = tb

    ## for data encode to packet
    ## AAAA
    def encodeCommandToPacket(self, command):
        return str(command)

    ## add Packet Preambel 
    def send_packet(self, client, command):
        client.sendall(self.encodeCommandToPacket(command))

    def init_strategy(self, strategy_slot):
        self.strategy_slot = strategy_slot

    def PacketServer(self):
        self.sock2 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock2.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY,1)
        self.sock2.bind(('127.0.0.1', 9007))
        self.sock2.listen(1)
        client, address = self.sock2.accept()
        self.client2 = client

    def strategyRun(self):
        while True:
            self.send_packet(self.clients[0], 1)
            "time.sleep(0.01)"
    
    def Initlisten(self):
        threading.Thread(target=self.PacketServer).start()
        self.sock.listen(2)
        client, address = self.sock.accept()
        self.clients.append(client)
        print("get connect")
        client.settimeout(None)
        threading.Thread(target=self.clientServer, args=(client,)).start()
        self.strategyRun()

    def decodePacket(self, packet, command):
        (pktno,) = struct.unpack('!H', packet[0:2])
        if pktno == 0 and packet[2] == str(command):
            return True 
        return False

   
# /////////////////////////////////////////////////////////////////////////////
#                                   main
# /////////////////////////////////////////////////////////////////////////////
mac = 0
def main():
    # instantiate the MAC
    mac = cs_mac(verbose=True)
    mac.Initlisten()    # don't expect this to return...

if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        pass
