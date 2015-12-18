/* Evelina Dumitrescu SCPD
 * XEN Covert Channel assignment
 */
#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <unistd.h>

#include <algorithm>
#include <bitset>
#include <iostream>
#include <fstream>
#include <memory>
#include <string>
#include <vector>

using namespace std;

typedef const char bit;
typedef unsigned short int word;

const int kAckSize = 16;
const int kCrcSize = sizeof(word) * 8;
const long kExpectedDuration = 1000000;
const int kIterations = (1 << 8) * 5;
const int kNACK = 0;
const char ONE = '1';
const int kPayloadSize = 8;
const string kReceiveEndpoint = "receive";
const string kSendEndpoint = "send";
const char ZERO = '0';

bool should_stop(long time1, long time2) {
  return (time2 - time1) > (kExpectedDuration + kExpectedDuration / 2);
}

long remaining_duration(long time1, long time2) {
  long duration = kExpectedDuration + (kExpectedDuration / 2) - (time2 - time1);

  if (duration > 0) {
    return duration;
  } else {
    return 0;
  }
}

long get_current_time() {
  struct timeval current_time;
  long usec = 0;

  gettimeofday(&current_time, NULL);
  usec = 1000000 * current_time.tv_sec + current_time.tv_usec;

  return usec;
}

void cpu_intensive_task() {
  auto time1 = get_current_time();

  for (int i = 0; i < kIterations; i++) {
    for (int j = 0; j < kIterations; j++) {
      auto time2 = get_current_time();
      if (should_stop(time1, time2)) {
        return;
      }
    }
  }
}

void do_nothing(long duration) {
  if (duration > 0) {
    usleep(duration);
  }
}

void calibrate(const long start_time) {
  auto duration = start_time - get_current_time();

  cout << "Calibrate sender and receiver" << endl << "Sleep for "
       << duration << " us" << endl;
  if (duration > 0) {
    usleep(duration);
  }
}

void send_bit(bit elem) {
 if (elem == ONE) {
   auto time1 = get_current_time();

   cpu_intensive_task();

   auto time2 = get_current_time();
   auto duration = remaining_duration(time1, time2);

   do_nothing(duration);
  } else if (elem == ZERO) {
    do_nothing(kExpectedDuration + kExpectedDuration / 2);
  }
}

bit receive_bit() {
  auto time1 = get_current_time();

  cpu_intensive_task();
  auto time2 = get_current_time();

  cout << "Received one bit in " << (time2 - time1) << " us" << endl;
  auto duration = remaining_duration(time1, time2);

  do_nothing(duration);
  if ((time2 - time1) > kExpectedDuration) {
    cout << "Estimate bit as " << ONE << endl;
    return ONE;
  } else {
    cout << "Estimate bit as " << ZERO << endl;
    return ZERO;
  }
}

string crc16(const char *data_p, unsigned int length)
{
  unsigned int crc = 0xffff;

  if (length == 0) {
    return bitset<kCrcSize>(~crc).to_string();
  }

  do
  {
    unsigned int data = (unsigned int)0xff & *data_p++;
    for (int i = 0; i < 8; i++, data >>= 1) {
      if ((crc & 0x0001) ^ (data & 0x0001)) {
        crc = (crc >> 1) ^ 0x8408;
      } else {
        crc >>= 1;
      }
    }
  } while (--length);

  crc = ~crc;
  crc = (crc << 8) | (crc >> 8 & 0xff);

  return bitset<kCrcSize>(crc).to_string();
}

bool check_crc(const string& frame) {
  const string crc = frame.substr(frame.size() - kCrcSize, kCrcSize);
  const string payload = frame.substr(0, frame.size() - kCrcSize);
  const string actual_crc = crc16(payload.c_str(), 10);
  cout << "Check crc" << endl;
  cout << "Crc: " << crc << endl;
  cout << "Payload: " << payload << endl;
  cout << "Actual crc: " << actual_crc << endl;
  cout << "Check if CRC is ok: " << (actual_crc == crc) << endl;
  return actual_crc == crc;
}

bool is_ack(const string& frame) {
  word bytes = (word)strtol(frame.substr(0, kAckSize).c_str(), NULL, 2);
  cout << "Check if packet is ack: " << (kNACK != bytes) << endl;
  return kNACK != bytes;
}

void send_frame(const string& line, const word frame_nr) {
  auto bits = line + bitset<kAckSize>(frame_nr).to_string();
  bits = bits + crc16(bits.c_str(), 10);
  cout << "Payload: " << line << endl;
  cout << "Frame nr: " << bitset<kAckSize>(frame_nr).to_string() << endl;
  cout << "Crc: " << crc16(bits.c_str(), 10) << endl;
  cout << "Frame: " << bits << endl;

  for (const char& bit : bits) {
    send_bit(bit);
  }
}

void send_ack(word frame_nr) {
  cout << "Send ACK" << endl;
  send_frame("", frame_nr);
}

void send_nack() {
  cout << "Send NACK" << endl;
  send_frame("", kNACK);
}

string receive_frame(const int frame_size) {
  string frame;

  for (int bit_nr = 0; bit_nr < frame_size; bit_nr++) {
    frame += receive_bit();
  }

  cout << "Receive frame " << frame << endl;
  return frame;
}

void read_bytes(const string& input_filename) {
  ifstream input_file(input_filename.c_str());
  string line;
  word frame_nr = 0;

  if (input_file.is_open()) {
    while (getline(input_file, line)) {
      if (frame_nr == 0xffff) {
        frame_nr = 1;
      } else {
        frame_nr ++;
      }
      cout << "Send new packet" << endl;
      send_frame(line, frame_nr);
      cout << "Wait for confirmation" << endl;
      bool received_ACK = false;

      while (!received_ACK) {
        auto frame = receive_frame(kAckSize + kCrcSize);

        received_ACK = is_ack(frame);
        if (!received_ACK) {
           send_frame(line, frame_nr);
        }
      }
    }

    input_file.close();
  }
}

void send(const string& input_filename, const long start_time) {
  cout << "I am sending frames" << endl;
  calibrate(start_time);
  read_bytes(input_filename);
}

void write_bytes(const string& output_filename, const vector<string>& lines) {
  ofstream output_file(output_filename.c_str());

  if (output_file.is_open())
  {
    for (const string& line : lines) {
      output_file << line;
    }
    output_file.close();
  }
}

void receive(
  const string& output_filename, const long start_time) {
  vector<string> result;
  word frame_nr = 0;

  cout << "I am receiving frames" << endl;
  calibrate(start_time);
  while (true) {
    auto frame = receive_frame(kPayloadSize + kAckSize + kCrcSize);

    while (check_crc(frame) != true) {
      send_nack();
      frame = receive_frame(kPayloadSize + kAckSize + kCrcSize);
    }

    auto chunk = frame.substr(kPayloadSize, kAckSize);
    cout << chunk << endl;
    word new_frame_nr = (word)strtol(chunk.c_str(), NULL, 2);

    send_ack(new_frame_nr);
    if (frame_nr == 0xffff) {
      frame_nr = 0;
    }
    if (new_frame_nr > frame_nr) {
      cout << "Accepted new frame: frame_nr " << new_frame_nr << " frame "
           << frame << endl;
      frame_nr = new_frame_nr;
      result.push_back(frame);
    }
  }
  write_bytes(output_filename, result);
}

int main(int argc, char** argv) {
  if (argc < 4) {
    cerr << "Wrong arguments.";
    return 1;
  } else {
    if (string(argv[1]) == kSendEndpoint) {
      send(argv[2], atol(argv[3]));
    } else if (string(argv[1]) == kReceiveEndpoint) {
      receive(argv[2], atol(argv[3]));
    }
  }

  cout << "Finished" << endl;
  return 0;
}
