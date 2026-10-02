import { Storage } from '@ionic/storage';

// Instância global do Ionic Storage
const storage = new Storage();

// Função para salvar dados no armazenamento
export const saveData = async (key: string, value: any): Promise<void> => {
  try {
    await storage.set(key, value);
  } catch (error) {
    console.error('Erro ao salvar dados:', error);
    throw error; // Trate o erro ou re-lance para tratar em outro lugar
  }
};

// Função para carregar dados do armazenamento
export const loadData = async (key: string): Promise<any> => {
  try {
    const data = await storage.get(key);
    return data;
  } catch (error) {
    console.error('Erro ao carregar dados:', error);
    throw error; // Trate o erro ou re-lance para tratar em outro lugar
  }
};