function returnedValue = getUniversal(varID, loopID)
%getUniversal An internal function to send the get command and read the 
%variable value.

%   Copyright 2015 Universidad Nacional de Colombia,
%   Politecnico Jaime Isaza Cadavid.
%   Authors: Andres Acosta, Jairo Espinosa, Jorge Espinosa.
%   $Id$

import traci.constants
global inductionloopSubscriptionResults

if isempty(inductionloopSubscriptionResults)
    returnValueFunc = traci.RETURN_VALUE_FUNC.inductionloop;
else
    returnValueFunc = inductionloopSubscriptionResults.valueFunc;
end

% Prepare the outgoing message and read the response. The result variable
% is a traci.Storage object
result = traci.sendReadOneStringCmd(constants.CMD_GET_INDUCTIONLOOP_VARIABLE,varID,loopID);
handleReturValueFunc = str2func(returnValueFunc(varID));

% Use the proper method to read the variable of interest from the result
returnedValue = handleReturValueFunc(result);