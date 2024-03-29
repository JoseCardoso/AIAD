function setTau(typeID, tau)
%setTau Sets the driver's reaction time for vehicles of this type.
%   setTau(TYPEID,TAU) Sets the driver's reaction time in s for vehicles of
%   this type.

%   Copyright 2015 Universidad Nacional de Colombia,
%   Politecnico Jaime Isaza Cadavid.
%   Authors: Andres Acosta, Jairo Espinosa, Jorge Espinosa.
%   $Id$

import traci.constants
traci.sendDoubleCmd(constants.CMD_SET_VEHICLETYPE_VARIABLE, constants.VAR_TAU, typeID, tau);