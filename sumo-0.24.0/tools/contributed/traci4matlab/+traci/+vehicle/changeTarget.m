function changeTarget(vehID, edgeID)
%changeTarget Change the vehicle's destination.
%   changeTarget(VEHID,EDGEID) Changes the vehicle's destination edge to
%   the given. The route is rebuilt.

%   Copyright 2015 Universidad Nacional de Colombia,
%   Politecnico Jaime Isaza Cadavid.
%   Authors: Andres Acosta, Jairo Espinosa, Jorge Espinosa.
%   $Id$

import traci.constants
traci.sendStringCmd(constants.CMD_SET_VEHICLE_VARIABLE, constants.CMD_CHANGETARGET, vehID, edgeID);