#!/bin/bash
if [ -f /root/.ssh/authorized_keys ]; then
  chmod 600 /root/.ssh/authorized_keys
fi

exec /usr/sbin/sshd -D
