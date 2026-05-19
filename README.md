# Grep for finding English*-Like Text
* Planned support for other languages.

Special thanks to https://cls.wortschatz-leipzig.de

This command line utility is designed to filter out gibberish and reasonably english-looking text from a stream of gibberish. For example, this can be used in PCAP analysis.
`cat myPcap.pcap | strings | fitnessGrep`

## Build
This project uses Gradle with Kotlin to build and text, and includes a Makefile to install the jar file with a shell script in order to use the utility conveniently. Java should be installed.
Quick build:
```bash
git clone https://github.com/ronondex2009/fitnessGrep
cd fitnessGrep
sudo make install
```

## Usage
By default, fitnessGrep will read from stdin and print only lines that pass as sufficiently english to stdout. This is currently the only supported mode of operation.
More modes of operation will be added soon.

`echo Hello | fitnessGrep`

## Planned
- Using sample file instead of internal quadgrams
- Command line arguments & help message
- Support for reading from files
- Custom fitness threshold and extra output formats (such as printing fitness instead of filtering.)

## Notice

app/src/main/resources/defaultEnglishQuadgramFrequencies.txt is obtained from Wortschatz Leipzig and is licensed under CC BY-NC 4.0 https://creativecommons.org/licenses/by-nc/4.0. This file is distributed along with this project but not distributed under the same terms as this project. Please see https://wortschatz.uni-leipzig.de/en/usage for terms of usage of their data.
T. Eckart & U. Quasthoff: Statistical Corpus and Language Comparison on Comparable Corpora.
In: Building and Using Comparable Corpora, Springer-Verlag Berlin Heidelberg, 2013, ISBN: 978-3-642-20128-8