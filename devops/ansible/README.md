## Requirements

```bash
python3 -m venv venv
source ./venv/bin/activate
pip install -f requirements.txt
mkdir library
cd library
git clone https://github.com/arenadata/ansible-module-yandex-cloud
cp ansible-module-yandex-cloud/modules/* .
rm -rf ansible-module-yandex-cloud
cd ../
```

## Usage

1. В директорию `keys/` расположить ключи `key-ansible.json` и `key.json` (директория находится в .gitignore)

2. В `~/.ssh/` положить ключ `devops_ed25519.pub`

3. Создать VM в Я.Облако:
```bash
export IAM_TOKEN="$(cat keys/key-ansible.json)"
export SSH_PUBLIC_KEY="$(cat ~/.ssh/devops_ed25519.pub)"
ansible-playbook -e "service_account_token=\"${IAM_TOKEN}\" ssh_public_key=\"${SSH_PUBLIC_KEY}\"" create-vm.yml
```

4. Любыми средствами узнать IP адрес созданной VM и добавить его в inventory.ini
```
[bbb]
<VM_IP> ansible_connection=ssh ansible_ssh_user=ops ansible_ssh_private_key_file=/home/<YOUR_USER>/.ssh/devops_ed25519 node_ip=<VM_IP>
```

5. Установка BigBlueButton + Greenlight на созданной VM:
```bash
ansible-playbook -i inventory.ini -e "bbb_hostname=<HOSTNAME> bbb_letsencrypt_email=<EMAIL> bbb_admin_password=<PASSWORD>" bbb.yml
```

6. Restart vm:
```bash
export IAM_TOKEN="$(cat keys/key-ansible.json)"
ansible-playbook -e "service_account_token=\"${IAM_TOKEN}\"" restart-vm.yml
```
