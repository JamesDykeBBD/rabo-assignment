FROM python:3.9-slim

# Install
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    postgresql-client \
    sshpass \
    python3-psycopg2 \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Install Ansible
RUN pip install --no-cache-dir ansible psycopg2-binary

# Create working directory
WORKDIR /ansible

# Create a non-root user to run Ansible
RUN useradd -m ansible
USER ansible

# Set environment variables
ENV ANSIBLE_HOST_KEY_CHECKING=False
