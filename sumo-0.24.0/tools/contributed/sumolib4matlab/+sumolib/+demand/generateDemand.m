%% generateDemand Generates vehicle demand in a SUMO-readable format.
%	generateDemand(netFile, flows, beginTime, endTime) Generates vehicle
%   demand for a SUMO network through origin-destination specification.
%   netFile is a string specifying the path where the network file is
%   located. flows is a cell array containing objects of the
%   sumolib.demand.Flow class. In this case, the Flow instances must define
%   a destination edge i.e their 'to' property must be non null. The demand
%   is generated by uniformly distributing the elements of the demand
%   property of the flowProfiles objects along the interval defined by
%   beginTime and endTime. The output files have the same name as the
%   network file and are created in the same directory.
%   generateDemand(..., turns) Generates demand through turning
%   probabilities at junctions. The turns parameter is an instance of
%   the sumolib.demand.TurnProbability that assigns a turn probability to 
%   the links connected to the given edge. Note that the flows should not
%   define a destination edge, otherwise it is ignored.
%   generateDemand(..., prefix). The output files are generated in the
%   directory defined by the string prefix, which also specifies the prefix
%   with which files are saved. It is recommended that the netFile and the
%   outputs reside in the same folder.

%   Copyright 2015 Universidad Nacional de Colombia,
%   Politecnico Jaime Isaza Cadavid.
%   Authors: Andres Acosta, Jairo Espinosa, Jorge Espinosa.
%	$Id$

function generateDemand(netFile, flows, beginTime, endTime,...
    varargin)

p = inputParser;

addRequired(p,'netFile',@ischar);
addRequired(p,'flows',@iscell);
addRequired(p,'beginTime',@isnumeric);
addRequired(p,'endTime',@isnumeric);
addOptional(p,'turns',{},@iscell);
addOptional(p,'prefix','',@ischar);

parse(p,netFile, flows, beginTime, endTime ,varargin{:});

% Disable warnings related to objett-to-struct conversion
warning('off', 'MATLAB:structOnObject');

netFile = p.Results.netFile;
flows = p.Results.flows;
beginTime = p.Results.beginTime;
endTime = p.Results.endTime;
turns = p.Results.turns;
prefix = p.Results.prefix;
hasTurns = 0;

if isempty(prefix)
    [~,netName,~] = fileparts(which(netFile));
    prefix = strrep([netName '_demand'], '.net', '');
end

% Validate if all flows have defined a destination edge if no turns are
% specified
if isempty(turns)
    for i = 1:length(flows)
        if isempty(flows{i}.to)
            departLaneStr = ['_' num2str(flows{i}.departLane)];
            if strcmp(departLaneStr, '_')
                departLaneStr = '';
            end
            throw(MException('sumolib:DefinirionError',...
                'No destination edge defined for flow %s.',...
                [flows{i}.from num2str(i-1) departLaneStr]));
        end
    end
else
    turnsFile = [prefix '.turns.xml'];
    hasTurns = 1;
end

numIntervals = length(flows{1}.demand);
time = linspace(beginTime,endTime, numIntervals + 1);

% timeStep = range(time)/numIntervals;
flowsFile = [prefix '.trips.xml'];
routesFile = [prefix '.rou.xml'];
vTypes = {};
busStops = {};

definedAddFile = 0;

%% Generate the additional file containing bus stops, if available
for i = 1:length(flows)
    if ~isempty(flows{i}.stop)
        stops = flows{i}.stop;
        for j = 1:length(stops)
            hasStopInLane = ~isempty(stops{j}.lane);
            if ~hasStopInLane
                if ~definedAddFile
                    addFile = [prefix '_bus_stops' '.add.xml'];
                    fileID = fopen(addFile,'w');
                    fprintf(fileID,'<add>\n');
                    definedAddFile = 1;
                end
                for k = 1:length(stops{j}.busStop)
                    currentBusStop = stops{j}.busStop{k};
                    if ~ismember(currentBusStop.id, busStops)
                        fprintf(fileID,'\t<busStop');
                        busStopStruct = struct(currentBusStop);
                        busStopStructFields = fieldnames(busStopStruct);
                        for l = 1:numel(busStopStructFields)
                            if ~isempty(busStopStruct.(char(busStopStructFields(l))))
                                fprintf(fileID,' %s="%s"', char(busStopStructFields(l)),...
                                    num2str(busStopStruct.(char(busStopStructFields(l)))));
                            end
                        end
                        busStops = [busStops currentBusStop.id];
                    end
                    fprintf(fileID,'/>\n');
                end
            end
        end
    end
end

if definedAddFile
    fprintf(fileID,'</add>');
    fclose(fileID);
end



%% Generate the trips file
fileID = fopen(flowsFile,'w');
fprintf(fileID,'<flows>\n');

% Generate vehicle types
for i = 1:length(flows)
    if ~isempty(flows{i}.type)
        if ~ismember(flows{i}.type.id, vTypes)
            vTypes = [vTypes {flows{i}.type.id}];
            typeStruct = struct(flows{i}.type);
            typeStructFields = fieldnames(typeStruct);
            fprintf(fileID,'\t<vType');
            for j = 1:numel(typeStructFields)
                if ~isempty(typeStruct.(char(typeStructFields(j))))
                    fprintf(fileID,' %s="%s"', char(typeStructFields(j)),...
                        typeStruct.(char(typeStructFields(j))));
                end
            end
            fprintf(fileID,'/>\n');
            vTypes = [vTypes flows{i}.type.id];
        end
    end
end

% Generate flows
for i=2:length(time)
	for j=1:length(flows)
        
        departLaneStr = ['_' num2str(flows{j}.departLane)];
        if strcmp(departLaneStr, '_')
            departLaneStr = '';
        end
        
        typeStr = ['_' flows{j}.type.id];
        if strcmp(typeStr, '_')
            typeStr = '';
        end

        flowID = [flows{j}.from num2str(i-1) departLaneStr typeStr];
        
        stops = {};
        flowStruct = struct(flows{j});
        flowStructFields = fieldnames(flowStruct);
        fprintf(fileID,'\t<flow id="%s" begin="%d" end="%d"',flowID,...
            time(i-1), time(i));
        for k = 1:numel(flowStructFields)
            if ~isempty(flowStruct.(char(flowStructFields(k))))
                if strcmp(flowStructFields(k), 'demandType')
                    fprintf(fileID,' %s=',...
                        flowStruct.(char(flowStructFields(k))));
                elseif strcmp(flowStructFields(k), 'demand')
                    demandArray = flowStruct.(char(flowStructFields(k)));
                    fprintf(fileID,'"%d"', demandArray(i-1));
                elseif strcmp(flowStructFields(k), 'number')
                    NumberArray = flowStruct.(char(flowStructFields(k)));
                    fprintf(fileID,' %s="%d"', char(flowStructFields(k)),...
                        NumberArray(i-1));
                elseif strcmp(flowStructFields(k), 'type')
                    flowVehType = flowStruct.(char(flowStructFields(k)));
                    fprintf(fileID,' %s="%s"', char(flowStructFields(k)),...
                        flowVehType.id);
                elseif strcmp(flowStructFields(k), 'stop')
                    stops = flowStruct.(char(flowStructFields(k)));  
                else
                    fprintf(fileID,' %s="%s"', char(flowStructFields(k)),...
                        num2str(flowStruct.(char(flowStructFields(k)))));
                end
            end
        end
        
        % Generate stops, if available
        if ~isempty(stops)
            fprintf(fileID,'>\n');
            for k = 1:length(stops)
                stopStruct = struct(stops{k});
                stopStructFields = fieldnames(stopStruct);
                fprintf(fileID,'\t\t<stop');
                for l = 1:numel(stopStructFields)
                    if ~isempty(stopStruct.(char(stopStructFields(l))))
                        if strcmp(stopStructFields(l), 'busStop')
                            fprintf(fileID,' %s="', char(stopStructFields(l)));
                            for m = 1:length(stops{k}.busStop)
                                fprintf(fileID,'%s', stops{k}.busStop{m}.id);
                                if m~=length(stops{k}.busStop)
                                    fprintf(fileID,' ', stops{k}.busStop{m}.id);
                                end
                            end
                            fprintf(fileID,'"');
                        else
                            fprintf(fileID,' %s="%s"', char(stopStructFields(l)),...
                                num2str(stopStruct.(char(stopStructFields(l)))));
                        end
                    end
                end
                fprintf(fileID,'/>\n');
                fprintf(fileID,'\t</flow>\n');
            end
        else
            fprintf(fileID,'/>\n');
        end
        

	end
end
fprintf(fileID,'</flows>');
fclose(fileID);

%% Generate the turns file and traffic demand
if hasTurns
    fileID = fopen(turnsFile,'w');
    fprintf(fileID,'<turns>\n\t<interval begin="%d" end="%d">\n',beginTime,...
        endTime);
    
    for i=1:length(turns)
        fprintf(fileID,'\t\t<fromEdge id="%s">\n', turns{i}.fromEdge);
        for j=1:length(turns{i}.toEdges)
            fprintf(fileID,'\t\t\t<toEdge id="%s" probability="%d"/>\n',...
                turns{i}.toEdges{j},turns{i}.probabilities{j});
        end
        fprintf(fileID,'\t\t</fromEdge>\n');
    end
    
    fprintf(fileID,'\t</interval>\n</turns>');
    fclose(fileID);
    system(['jtrrouter --net-file ' netFile ' --flow-files ' flowsFile ' --turn-ratio-files ' turnsFile ' --output-file ' routesFile ' --ignore-errors' ' -v']);
else
    if ~definedAddFile
        system(['duarouter --net-file ' netFile ' --flow-files ' flowsFile ' --output-file ' routesFile ' --ignore-errors' ' -v']);
    else
        system(['duarouter --net-file ' netFile ' --flow-files ' flowsFile ' --additional-files ' addFile ' --output-file ' routesFile ' --ignore-errors' ' -v']);
    end
end