function setSpeedDeviation(vehID, deviation)
%setSpeedDeviation Sets the maximum speed deviation for this vehicle.
%   setSpeedDeviation(VEHID,DEVIATION) Sets the maximum speed deviation for 
%   this vehicle.

%   Copyright 2015 Universidad Nacional de Colombia,
%   Politecnico Jaime Isaza Cadavid.
%   Authors: Andres Acosta, Jairo Espinosa, Jorge Espinosa.
%   $Id$

import traci.constants
traci.sendDoubleCmd(constants.CMD_SET_VEHICLE_VARIABLE, constants.VAR_SPEED_DEVIATION, vehID, deviation);