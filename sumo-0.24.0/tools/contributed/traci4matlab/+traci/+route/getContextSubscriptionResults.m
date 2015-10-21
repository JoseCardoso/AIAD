function ContextSubscriptionResults = getContextSubscriptionResults(routeID)
%getContextSubscriptionResults Get the context subscription results for the
%   last time step.
%   contextSubscriptionResults = getContextSubscriptionResults(ROUTEID) 
%   Returns the context subscription results for the last time step and the
%   given route. If no route id is given, all subscription results are 
%   returned in a containers.Map data struccure. If the route id is unknown 
%   or the subscription did for any reason return no data, 'None' is 
%   returned.
%   It is not possible to retrieve older subscription results than the ones
%   from the last time step.

%   Copyright 2015 Universidad Nacional de Colombia,
%   Politecnico Jaime Isaza Cadavid.
%   Authors: Andres Acosta, Jairo Espinosa, Jorge Espinosa.
%   $Id$

global routeSubscriptionResults
if isempty(routeSubscriptionResults)
    throw(MException('traci:FatalTraCIError',...
        'You have to subscribe to the variable'));
end
if nargin < 1
    routeID=None;
end
ContextSubscriptionResults = routeSubscriptionResults.getContext(routeID);