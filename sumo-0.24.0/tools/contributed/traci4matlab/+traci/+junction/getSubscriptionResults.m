function subscriptionResults = getSubscriptionResults(junctionID)
%getSubscriptionResults Get the subscription results for the last time step.
%   subscriptionResults = getSubscriptionResults(JUNCTIONID) Returns the 
%   subscription results for the last time step and the given junction. If no 
%   junction id is given, all subscription results are returned in a 
%   containers.Map data structure.
%   If the junction id is unknown or the subscription did for any reason return
%   no data, 'None' is returned.
%   It is not possible to retrieve older subscription results than the ones
%   from the last time step.

%   Copyright 2015 Universidad Nacional de Colombia,
%   Politecnico Jaime Isaza Cadavid.
%   Authors: Andres Acosta, Jairo Espinosa, Jorge Espinosa.
%   $Id$

global junctionSubscriptionResults
if isempty(junctionSubscriptionResults)
    throw(MException('traci:FatalTraCIError',...
        'You have to subscribe to the variable'));
end
if nargin < 1
    junctionID = 'None';
end

subscriptionResults = junctionSubscriptionResults.get(junctionID);