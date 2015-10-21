#!/usr/bin/env python
"""
@file    route2trips.py
@author  Jakob Erdmann
@date    2015-08-05
@version $Id: route2sel.py 18756 2015-08-31 19:16:33Z behrisch $

This script converts SUMO routes into an edge selection

SUMO, Simulation of Urban MObility; see http://sumo.dlr.de/
Copyright (C) 2008-2015 DLR (http://www.dlr.de/) and contributors

This file is part of SUMO.
SUMO is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.
"""
from __future__ import print_function
import sys
from optparse import OptionParser
from sumolib.output import parse_fast


def parse_args():
    USAGE = "Usage: " + sys.argv[0] + " <routefile> [options]"
    optParser = OptionParser()
    optParser.add_option("-o", "--outfile", help="name of output file")
    options, args = optParser.parse_args()
    try:
        options.routefile, = args
    except:
        sys.exit(USAGE)
    if options.outfile is None:
        options.outfile = options.routefile + ".sel.txt"
    return options


def main():
    options = parse_args()
    edges = set()
    for route in parse_fast(options.routefile, 'route', ['edges']):
        edges.update(route.edges.split())

    with open(options.outfile, 'w') as outf:
        for e in sorted(list(edges)):
            outf.write('edge:%s\n' % e)

if __name__ == "__main__":
    main()
