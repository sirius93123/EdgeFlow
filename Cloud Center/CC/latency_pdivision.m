clear all;
clc;
lambda1=1;
lambda2=1;
theta=0.1;
c11=1.15;
c12=1.15;
c2=5;
r0=4;
l_mem=zeros(99,99);
for p11=0.01:0.01:0.99
    for p12=0.01:0.01:0.99
        p21=1-p11;
        p22=1-p12;
        l=p11*lambda1/c11+p12*lambda2/c12+(sqrt(p21*lambda1)+sqrt(p22*lambda2))^2/c2+(sqrt(lambda1*(theta*p11+p21))+sqrt(lambda2*(theta*p12+p22)))^2/r0;
        n1=round(p11/0.01);
        n2=round(p12/0.01);
        l_mem(n1,n2)=l;
    end;
end;
p11=linspace(0.01,0.99,99);
p12=linspace(0.01,0.99,99);
surf(p11,p12,l_mem);
contourf(p11,p12,l_mem);
% figure(2);
% hold on;
% for n1=10:10:90
%     p12=0.01:0.01:0.99;
%         plot(p12,l_mem(n1,:));
% end;

