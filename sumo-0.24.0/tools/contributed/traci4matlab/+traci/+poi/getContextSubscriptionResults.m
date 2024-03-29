function ContextSubscriptionResults = getContextSubscriptionResults(poiID)
%getContextSubscriptionResults Get the context subscription results for the
%   last time step.
%   contextSubscriptionResults = getContextSubscriptionResults(POIID) 
%   Returns the context subscription results for the last time step and the
%   given poi. If no poi id is given, all subscription results are 
%   returned in a containers.Map data struccure. If the poi id is unknown 
%   or the subscription did for any reason return no data, 'None' is 
%   returned.
%   It is not possible to retrieve older subscription results than the ones
%   from the last time step.

%   Copyright 2015 Universidad Nacional de Colombia,
%   Politecnico Jaime Isaza Cadavid.
%   Authors: Andres Acosta, Jairo Espinosa, Jorge Espinosa.
%   $Id$

global poiSubscriptionResults
if isempty(poiSubscriptionResults)
    throw(MException('traci:FatalTraCIError',...
        'You have to subscribe to the variable'));
end
if nargin < 1
    poiID=None;
end
ContextSubscriptionResults = poiSubscriptionResults.getContext(poiID);