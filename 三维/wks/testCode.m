b=offread('T4.off');
ver=b{4};
face=b{5};
temp=face(2:4,:);
[N D]=size(temp);

face=temp+ones(N,D);
clear temp;
[WKS,E,PHI,L] = compute_wks(ver',face');