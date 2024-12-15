package com.example.Backend.Services;

import com.example.Backend.Dto.Requests.ClasseRequest;
import com.example.Backend.Dto.Responses.ClasseResponse;
import com.example.Backend.Entities.Classe;
import com.example.Backend.Exceptions.NotFoundException;
import com.example.Backend.Repositories.ClasseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClasseService {
    private final ClasseRepository classeRepository;
    public ClasseResponse createClasse(ClasseRequest request) throws IOException {
        Classe classe=Classe.builder()
                .nom(request.getNom())
                .build();
        classe=classeRepository.save(classe);
        return response(classe);


    }
    public ClasseResponse updateClasse(ClasseRequest request,Long classeId) throws IOException {
        Optional<Classe> classe =classeRepository.findById(classeId);
        if(classe.isPresent()){
            classe.get().setNom(request.getNom());
            var res=classeRepository.save(classe.get());
            return response(res);

        }else {
            throw new NotFoundException("classe  avec l'id "+classeId+" n'existe pas");
        }
    }
    public void deleteClasse(Long classeId)  {
        Optional<Classe> classe =classeRepository.findById(classeId);
        if(classe.isPresent()){
            classeRepository.deleteById(classeId);

        }else {
            throw new NotFoundException("classe avec l'id "+classeId+" n'existe pas");
        }
    }
    public List<ClasseResponse> allClasses() throws IOException {
        List<Classe> classes=classeRepository.findAll();
        List<ClasseResponse> classeResponses=new ArrayList<>();
        for(Classe classe:classes){
            var res=response(classe);
            classeResponses.add(res);
        }
        return classeResponses;
    }

    public static  ClasseResponse response(Classe classe) throws IOException {
        return ClasseResponse.builder()
                .id(classe.getId())
                .nom(classe.getNom())
                .build();
    }
}
