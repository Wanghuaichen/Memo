function [] = drawExtrema( img, extrema, range )
%DRAWEXTREMA Summary of this function goes here
%   Detailed explanation goes here
figure;
imshow(img, range);
[x,y] = size(img);

for j = 1:y,
    for i = 1:x,
        if (extrema(i,j) == -1),
            line('xdata',[j-1 j+1], 'ydata',[i-1 i-1], 'color', 'red');
            line('xdata',[j-1 j+1], 'ydata',[i+1 i+1], 'color', 'red');
            line('xdata',[j-1 j-1], 'ydata',[i-1 i+1], 'color', 'red');
            line('xdata',[j+1 j+1], 'ydata',[i-1 i+1], 'color', 'red');
        end
        if (extrema(i,j) == 1),
            line('xdata',[j-1 j+1], 'ydata',[i-1 i-1], 'color', 'green');
            line('xdata',[j-1 j+1], 'ydata',[i+1 i+1], 'color', 'green');
            line('xdata',[j-1 j-1], 'ydata',[i-1 i+1], 'color', 'green');
            line('xdata',[j+1 j+1], 'ydata',[i-1 i+1], 'color', 'green');
        end
        
    end
end


end

