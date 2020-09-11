###############################################################################
# Simple script which collects all .txt docs for custom steps and updates
# the associated README.md in "vars/"
###############################################################################

import os
import sys
import glob

def add_line(md, line):
  md = md + line + '\n'
  return md

# Set current directory to the directory of the script.
root = os.path.dirname(os.path.realpath(__file__))
os.chdir(root)

# Create header and gather all step documentation files.
markdown = '# Custom Pipeline Steps\n'
files = glob.glob('../vars/*.txt')

# Use file name for step subheader and concatenate out step doc
# content.
for f in files:
  step = os.path.splitext(os.path.basename(f))[0]
  markdown = add_line(markdown, "## `{}`".format(step))

  with open(f, 'r') as content:
    markdown = add_line(markdown, content.read().replace("\n\n", "\n") + "\n")

# Write out content to README.md within steps folder.
with open('../vars/README.md', 'w') as r:
  r.write(markdown)