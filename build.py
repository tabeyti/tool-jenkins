import os
import sys
from optparse import OptionParser

parser = OptionParser()
parser.add_option("-m", "--message", dest="message", help="Message.")
parser.add_option("-t", "--thing", dest="thing", help="Thing.")
(options, args) = parser.parse_args()

print "The message is thus: {}".format(options.message) 
print "Heres the thing...{}".format(options.thing)

if not os.path.exists(options.thing):
  print "FILE NOT DER: {}".format(options.thing)
  with open(options.thing, "w") as myfile: myfile.write("appended text")
  sys.exit(1)

